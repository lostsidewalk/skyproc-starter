package skyprocstarter;

import lev.gui.LCheckBox;
import lev.gui.LTextPane;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;

import java.util.function.Function;

@Configuration
public class YourPatcherConfig {

    @Bean(name = "welcomePanelFactory")
    Function<SPMainMenuPanel, SPSettingPanel> getWelcomePanelFactory() {
        return parent -> new SPSettingPanel(parent, SkyProcStarter.myPatchName, SkyProcStarter.headerColor) {

            LTextPane introText;

            @Override
            protected void initialize () {
                super.initialize();

                introText = new LTextPane(settingsPanel.getWidth() - 40, 400, SkyProcStarter.settingsColor);
                introText.setText(SkyProcStarter.welcomeText);
                introText.setEditable(false);
                introText.setFont(SkyProcStarter.settingsFont);
                introText.setCentered();
                setPlacement(introText);
                Add(introText);

                alignRight();
            }
        };
    }

    @Bean(name = "otherSettingsPanelFactory")
    Function<SPMainMenuPanel, SPSettingPanel> getOtherSettingsPanelFactory() {
        return parent -> new SPSettingPanel(parent, "Other Settings", SkyProcStarter.headerColor) {

            LCheckBox importOnStartup;

            @Override
            protected void initialize() {
                super.initialize();

                importOnStartup = new LCheckBox("Import Mods on Startup", SkyProcStarter.settingsFont, SkyProcStarter.settingsColor);
                importOnStartup.tie(YourSaveFile.Settings.IMPORT_AT_START, SkyProcStarter.save, SUMGUI.helpPanel, true);
                importOnStartup.setOffset(2);
                importOnStartup.addShadow();
                setPlacement(importOnStartup);
                AddSetting(importOnStartup);

                alignRight();
            }
        };
    }
}
