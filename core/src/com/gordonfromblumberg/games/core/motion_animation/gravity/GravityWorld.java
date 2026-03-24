package com.gordonfromblumberg.games.core.motion_animation.gravity;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.gordonfromblumberg.games.core.common.factory.AbstractFactory;
import com.gordonfromblumberg.games.core.common.physics.AccelerationMovingStrategy;
import com.gordonfromblumberg.games.core.common.utils.FileUtils;
import com.gordonfromblumberg.games.core.common.utils.Paths;
import com.gordonfromblumberg.games.core.common.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GravityWorld extends World {
    private static final float DELAY = 1f / 60;
    private static final float G = AbstractFactory.getInstance().configManager().getFloat("gravity.gConstant");
    private static final Vector2 tempV2 = new Vector2();
    private static final FileHandle saveDir = Paths.workDir().child("saves");

    private final AccelerationMovingStrategy ams = new AccelerationMovingStrategy();

    final IntMap<SpaceBody> bodyMap = new IntMap<>();
    final Array<SpaceBody> bodies = new Array<>();
    private final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1 << 14);

    SpaceBody underPointer;
    boolean underPointerChanged;

    private float time;
    private int nextId = 1;

    @Override
    public void initialize() {
        saveDir.mkdirs();
        ams.setTurnInVelocityDirection(false);

        SpaceBody sun = new SpaceBody();
        sun.mass = 2000f;
        sun.setSize(50f);
        sun.setPosition(25f, 25f);
        sun.setColor(0.75f, 0.75f, 0.15f, 1f);
        addBody(sun);
        
        SpaceBody earth = new SpaceBody();
        earth.mass = 40f;
        earth.setSize(25f);
        earth.velocity.set(0f, 40f);
        earth.setPosition(1200f, 25f);
        earth.setColor(0.2f, 0.5f, 0.55f, 1f);
        addBody(earth);

        SpaceBody moon = new SpaceBody();
        moon.mass = 1f;
        moon.setSize(10f);
        moon.velocity.set(10f, 60f);
        moon.setPosition(1130f, 25f);
        moon.setColor(0.5f, 0.3f, 0.25f, 1f);
        addBody(moon);

        SpaceBody merc = new SpaceBody();
        merc.mass = 10f;
        merc.setSize(15f);
        merc.velocity.set(0f, 90f);
        merc.setPosition(200f, 25f);
        merc.setColor(0.55f, 0.3f, 0.25f, 1f);
        addBody(merc);

        SpaceBody venera = new SpaceBody();
        venera.mass = 30f;
        venera.setSize(22f);
        venera.velocity.set(0f, 60f);
        venera.setPosition(500f, 25f);
        venera.setColor(0.5f, 0.35f, 0.10f, 1f);
        addBody(venera);

        SpaceBody blackHole = new SpaceBody();
        blackHole.mass = 1000000f;
        blackHole.setSize(55f);
        blackHole.velocity.set(10f, 0f);
        blackHole.setPosition(0f, 20000f);
        blackHole.setColor(0.f, 0.f, 0.f, 1f);

        saveToContinue();
    }

    @Override
    public void update(float delta, float mouseX, float mouseY) {
        super.update(delta, mouseX, mouseY);

        SpaceBody prev = underPointer;
        underPointer = null;
        for (SpaceBody sb : bodies) {
            float r2 = sb.getWidth() / 2;
            r2 *= r2;
            if (sb.position.dst2(mouseX, mouseY) <= r2)
                underPointer = sb;
        }
        underPointerChanged = underPointer != prev;

        if (!paused) {
            time += delta;

            if (time < DELAY) return;

            time = 0;

            final int count = bodies.size;
            final Vector2 temp = tempV2;
            final float g = G;
            for (int i = 0; i < count; ++i) {
                SpaceBody sb1 = bodies.get(i);
                for (int j = i + 1; j < count; ++j) {
                    SpaceBody sb2 = bodies.get(j);
                    temp.set(sb1.position).sub(sb2.position);
                    float r2 = temp.len2();
                    temp.nor();
                    sb2.acceleration.mulAdd(temp, g * sb1.mass / r2);
                    sb1.acceleration.mulAdd(temp, -g * sb2.mass / r2);
                }
            }

            for (SpaceBody sb : bodies) {
                sb.update(DELAY);
                sb.acceleration.setZero();
            }
        }
    }

    void addBody(SpaceBody sb) {
        SpaceBody existing = bodyMap.get(sb.getId());
        if (existing != null) {
            if (existing == sb) return;
            throw new IllegalArgumentException("Body with id " + sb.getId() + " already exists!");
        }
        if (sb.getId() < 1) sb.setId(nextId++);

        sb.setMovingStrategy(ams);
        bodies.add(sb);
        bodyMap.put(sb.getId(), sb);
    }

    @Override
    protected void save(ByteBuffer bb) {
        int count = bodies.size;
        bb.putInt(count);
        for (int i = 0; i < count; ++i) {
            bodies.get(i).save(bb);
        }
    }

    @Override
    protected void load(ByteBuffer bb) {
        clear();

        int count = bb.getInt();
        for (int i = 0; i < count; ++i) {
            SpaceBody sb = SpaceBody.instance();
            sb.load(bb);
            addBody(sb);
        }
    }

    void saveToContinue() {
        byteBuffer.clear();
        save(byteBuffer);
        byteBuffer.flip();
        File file = saveDir.child("continue.sav").file();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create 'continue.sav'", e);
        }
        FileUtils.saveToFile(byteBuffer, file);
    }

    void loadContinue() {
        File file = saveDir.child("continue.sav").file();
        if (!file.exists()) return;

        byteBuffer.clear();
        FileUtils.readFile(byteBuffer, file);
        byteBuffer.flip();
        load(byteBuffer);
    }

    void clear() {
        nextId = 1;
        bodyMap.clear();
        for (SpaceBody sb : bodies) {
            sb.release();
        }
        bodies.clear();
    }
}
