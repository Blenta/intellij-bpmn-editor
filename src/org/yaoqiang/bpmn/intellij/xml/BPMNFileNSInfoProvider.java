package org.yaoqiang.bpmn.intellij.xml;

import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlFileNSInfoProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.bpmn.intellij.editor.BPMNFileTypeFactory;
import org.yaoqiang.model.XMLConstants;
import org.yaoqiang.model.bpmn.BPMNModelConstants;

/**
 * Created by Blenta on 5/31/2017.
 */
public class BPMNFileNSInfoProvider implements XmlFileNSInfoProvider {

    private static final String[][] NAMESPACES = {
            {"", BPMNModelConstants.BPMN_SEMANTIC_MODEL_NS},
            {"bpmndi", BPMNModelConstants.BPMN_DI_NS},
            {"di", BPMNModelConstants.DI_NS},
            {"dc", BPMNModelConstants.DC_NS},
            {"xsi", XMLConstants.XMLNS_XSI},
            {"xsd", XMLConstants.XMLNS_XSD},
            {"yaoqiang", XMLConstants.YAOQIANG_NS}};

    @Nullable
    @Override
    public String[][] getDefaultNamespaces(@NotNull XmlFile file) {
        if (BPMNFileTypeFactory.isBPMN(file)) {
            return NAMESPACES;
        } else {
            return null;
        }
    }

    @Override
    public boolean overrideNamespaceFromDocType(@NotNull XmlFile file) {
        return false;
    }
}
