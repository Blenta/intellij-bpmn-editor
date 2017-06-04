package org.yaoqiang.bpmn.intellij.options;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.yaoqiang.asaf.ASAF;
import org.yaoqiang.asaf.ResourceMap;
import org.yaoqiang.asaf.StaticActionMap;
import org.yaoqiang.asaf.panel.ComboItem;
import org.yaoqiang.asaf.panel.ComboPanel;
import org.yaoqiang.asaf.panel.PanelContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Blenta on 6/3/2017.
 */
public class BPMNFileEditorMainConfigurable implements Configurable {

    private ComboPanel languagePanel;

    private boolean modified = false;

    @Nls
    @Override
    public String getDisplayName() {
        return "BPMN Editor";
    }

    @Nullable
    @Override
    public JComponent createComponent() {

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        Map<String, String> locales = new HashMap<String, String>();

        for (String l : ASAF.SUPPORT_LANGUAGES) {
            Locale locale = null;
            int index = l.indexOf('_');
            if (index > 0) {
                locale = new Locale(l.substring(0, index), l.substring(index + 1));
            } else {
                locale = new Locale(l);
            }
            locales.put(l, locale.getDisplayName());
        }

        languagePanel = new ComboPanel(new PanelContainer(), null, "language", locales.entrySet(), 80, false, false, true);
        String currentLanguage = ASAF.getSetting(ASAF.KEY_LANGUAGE, ASAF.DEFAULT_LANGUAGE);
        languagePanel.setSelectedItem(new ComboItem(new AbstractMap.SimpleEntry<String, String>(currentLanguage, locales.get(currentLanguage))));
        languagePanel.addActionListener(listener);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(languagePanel, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        return mainPanel;
    }


    private ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Map.Entry<String, String> selected = (Map.Entry<String, String>) ((ComboItem) languagePanel.getSelectedItem()).getObject();
            if (selected.getKey().equals(ASAF.getSetting(ASAF.KEY_LANGUAGE, ASAF.DEFAULT_LANGUAGE))) {
                return;
            }
            ASAF.setSetting(ASAF.KEY_LANGUAGE, selected.getKey());
            String language = selected.getKey();
            int index = language.indexOf('_');
            Locale locale = null;
            if (index > 0) {
                locale = new Locale(language.substring(0, index), language.substring(index + 1));
            } else {
                locale = new Locale(language);
            }
            ASAF.setLocale(locale);
            System.setProperty("asaf.language", language);
            ResourceMap.refresh();

            ASAF.refreshActions();
            StaticActionMap.resetActions();
            // modified = true;
        }

    };

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void apply() throws ConfigurationException {
        modified = false;
    }
}
