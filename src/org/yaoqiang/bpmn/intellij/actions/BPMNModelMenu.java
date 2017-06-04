package org.yaoqiang.bpmn.intellij.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import org.yaoqiang.asaf.ASAF;

/**
 * Created by Blenta on 6/3/2017.
 */
public class BPMNModelMenu extends DefaultActionGroup {

    @Override
    public void update(AnActionEvent event) {
        event.getPresentation().setEnabledAndVisible(ASAF.getSelectedFileTab() != null);
    }

}
