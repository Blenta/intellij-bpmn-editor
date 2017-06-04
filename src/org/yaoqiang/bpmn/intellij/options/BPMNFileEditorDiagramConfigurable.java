package org.yaoqiang.bpmn.intellij.options;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.asaf.panel.Panel;
import org.yaoqiang.asaf.panel.PanelContainer;
import org.yaoqiang.bpmn.editor.panels.BPMNEditorPanelFactory;

import javax.swing.*;
import java.awt.*;


/**
 * Created by Blenta on 6/3/2017.
 */
public class BPMNFileEditorDiagramConfigurable implements Configurable {

    private Panel diagramPanel;

    @Nls
    @Override
    public String getDisplayName() {
        return "Diagram";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        PanelContainer panelContainer = new PanelContainer();
        BPMNEditorPanelFactory factory = BPMNEditorPanelFactory.getInstance();
        factory.setPanelContainer(panelContainer);
        diagramPanel = factory.createPanel(null, BPMNEditorPanelFactory.DIAGRAM_SETTINGS);
        mainPanel.add(diagramPanel, BorderLayout.NORTH);

        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return diagramPanel.getPanelContainer().isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        diagramPanel.saveObjects();
        diagramPanel.getPanelContainer().setModified(false);
    }
}
