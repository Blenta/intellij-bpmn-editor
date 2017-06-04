package org.yaoqiang.bpmn.intellij.toolwindow;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import org.jetbrains.annotations.NotNull;
import org.yaoqiang.bpmn.editor.views.palette.ElementsPalette;
import org.yaoqiang.bpmn.editor.views.palette.PalettePane;

/**
 * Created by Blenta on 5/30/2017.
 */
public class BPMNPalette extends SimpleToolWindowPanel implements Disposable {

    private Project myProject;

    public BPMNPalette(@NotNull Project project) {
        super(true, true);
        myProject = project;
        PalettePane elements = new ElementsPalette();
        elements.initArtifactsPalette();
        add(elements);
    }

    @Override
    public void dispose() {
        myProject = null;
    }
}
