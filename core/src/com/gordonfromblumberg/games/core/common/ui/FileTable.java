package com.gordonfromblumberg.games.core.common.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import static com.gordonfromblumberg.games.core.common.utils.StringUtils.isBlank;

public class FileTable extends Table {
    private static final float MODIFIED_COL_WIDTH = 80f;

    private final ClickListener onPathClickListener = new ClickListener(Input.Buttons.LEFT) {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);

            Label target = (Label) event.getListenerActor();
            if (target.getUserObject() instanceof File newDir) {
                open(newDir);
                event.stop();
            }
        }
    };

    private final HorizontalGroup pathWidget = new HorizontalGroup().wrap().reverse();
    private final Table content = new Table();
    private File currentDir;
    private String extension;
    private final FileFilter extensionFilter = file -> file.isDirectory() || file.getName().endsWith(extension);
    private FileTableStyle style;

    public FileTable(Skin skin) {
        this(skin, new File(""), null);
    }

    public FileTable(Skin skin, File currentDir, String extension) {
        super(skin);

        add(pathWidget).colspan(2).expandX().fillX();

        row().spaceTop(15f);
        add("Name").expandX().align(Align.center);
        add("Modified").width(MODIFIED_COL_WIDTH).align(Align.center);

        row();
        add(new ScrollPane(content, skin)).expand().fill();
        content.defaults().spaceTop(2f).align(Align.left);
        content.columnDefaults(2).width(MODIFIED_COL_WIDTH);

        if (currentDir != null) {
            open(currentDir);
        }

        if (!isBlank(extension)) {
            this.extension = '.' + extension;
        }

        setStyle(skin.get(FileTableStyle.class));
    }

    void open(File directory) {
        setCurrentDir(directory);

        fillPathWidget();
        fillContent();
    }

    void fillPathWidget() {
        pathWidget.clear();

        File dir = currentDir;
        while (dir != null) {
            File parent = dir.getParentFile();
            if (parent != null) {
                Label label = new Label(dir.getName() + "/", getSkin());
                if (dir != currentDir) {
                    label.setUserObject(dir);
                    label.addListener(onPathClickListener);
                }
                pathWidget.addActor(label);
            }
            dir = parent;
        }

        File[] roots = File.listRoots();
        if (roots.length > 1) {
            SelectBox<File> selectBox = new SelectBox<>(getSkin());
            selectBox.setItems(roots);
            pathWidget.addActor(selectBox);

        } else {
            File root = roots[0];
            Label rootLabel = new Label(root.getPath(), getSkin());
            if (root != currentDir) {
                rootLabel.setUserObject(root);
                rootLabel.addListener(onPathClickListener);
            }
            pathWidget.addActor(rootLabel);
        }
    }

    void fillContent() {

    }

    private void setCurrentDir(File directory) {
        try {
            this.currentDir = directory.getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't to get canonical path of directory " + directory, e);
        }
    }

    public FileTableStyle getStyle() {
        return style;
    }

    public void setStyle(FileTableStyle style) {
        this.style = style;

        setBackground(style.background);
    }

    public static class FileTableStyle {
        public Drawable background;
    }
}
