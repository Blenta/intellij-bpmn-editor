package org.yaoqiang.bpmn.intellij.options;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.asaf.ASAF;
import org.yaoqiang.asaf.panel.Panel;
import org.yaoqiang.asaf.panel.PanelContainer;
import org.yaoqiang.bpmn.editor.panels.BPMNEditorPanelFactory;
import org.yaoqiang.bpmn.editor.views.BPMNFileTab;

import javax.swing.*;
import java.awt.*;


/**
 * Created by Blenta on 6/3/2017.
 */
public class BPMNFileEditorElementStylesConfigurable implements Configurable {

    private Panel elementStylesPanel;

    @Nls
    @Override
    public String getDisplayName() {
        return "Element Styles";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        if (!(ASAF.getSelectedFileTab() instanceof BPMNFileTab)) {
            mainPanel.add(new JLabel("Please open an OMG BPMN 2.0 XML file to edit its settings."));
        } else {
            PanelContainer panelContainer = new PanelContainer();
            BPMNEditorPanelFactory factory = BPMNEditorPanelFactory.getInstance();
            factory.setPanelContainer(panelContainer);
            elementStylesPanel = factory.createPanel(null, BPMNEditorPanelFactory.ELEMENT_STYLES);
            mainPanel.add(elementStylesPanel, BorderLayout.NORTH);
        }

        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return elementStylesPanel == null ? false : elementStylesPanel.getPanelContainer().isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        elementStylesPanel.saveObjects();
        elementStylesPanel.getPanelContainer().setModified(false);
    }
}
