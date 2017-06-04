package org.yaoqiang.bpmn.intellij.actions;

import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.actions.CreateFromTemplateAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Blenta on 5/31/2017.
 */
public class NewBPMNFileAction extends CreateFromTemplateAction {

    private static final String INTERNAL_TEMPLATE_NAME = "BPMN 2.0 XML.bpmn";

    public static final Key<Boolean> NEW_CREATED = Key.create("newCreated");

    public NewBPMNFileAction() {
        super(FileTemplateManager.getDefaultInstance().getInternalTemplate(INTERNAL_TEMPLATE_NAME));
    }

    @Nullable
    @Override
    protected Map<String, String> getLiveTemplateDefaults(DataContext dataContext, @NotNull PsiFile file) {
        file.getVirtualFile().putUserData(NEW_CREATED, true);
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("SYSTEM_TIME", "_" + System.currentTimeMillis());
        variables.put("DEF_NAME", "");
        return variables;
    }
}
