package com.gordonfromblumberg.games.core.motion_animation.gravity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.gordonfromblumberg.games.core.common.model.PhysicsGameObject;

import java.nio.ByteBuffer;

public class SpaceBody extends PhysicsGameObject {
    private static final Pool<SpaceBody> pool = new Pool<>() {
        @Override
        protected SpaceBody newObject() {
            return new SpaceBody();
        }
    };

    private final Color tempClr = new Color();

    String name;
    float mass;

    public SpaceBody() {
        setRegion("white-circle");
    }

    public static SpaceBody instance() {
        return pool.obtain();
    }

    void save(ByteBuffer bb) {
        bb.putInt(getId());
        bb.putFloat(mass);
        bb.putFloat(getWidth());
        bb.putFloat(position.x);
        bb.putFloat(position.y);
        bb.putFloat(velocity.x);
        bb.putFloat(velocity.y);
        bb.putFloat(rotation.x);
        bb.putFloat(rotation.y);
        bb.putInt(Color.rgba8888(getColor()));
    }

    void load(ByteBuffer bb) {
        setId(bb.getInt());
        mass = bb.getFloat();
        setSize(bb.getFloat());
        float x = bb.getFloat();
        float y = bb.getFloat();
        setPosition(x, y);
        velocity.x = bb.getFloat();
        velocity.y = bb.getFloat();
        rotation.x = bb.getFloat();
        rotation.y = bb.getFloat();
        Color.rgba8888ToColor(tempClr, bb.getInt());
        setColor(tempClr);
    }

    public void setSize(float size) {
        setSize(size, size);
    }

    public String getName() {
        return name;
    }

    public float getMass() {
        return mass;
    }

    @Override
    public void reset() {
        super.reset();

        this.mass = 0;
    }

    @Override
    public void release() {
        pool.free(this);
    }
}
