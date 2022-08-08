package skyprocstarter;

import lev.gui.LCheckBox;
import lev.gui.LTextPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import skyproc.Mod;
import skyproc.SkyProcSave;
import skyproc.gui.SPMainMenuPanel;
import skyproc.gui.SPSettingPanel;
import skyproc.gui.SUMGUI;
import skyprocstarter.SkyProcStarter.SettingsPanelDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The important functions to change are:
 * - welcomePanelFactory, otherSettingsPanelFactory, where you set up the GUI
 * - customPatcher, where you put all the processing code and add records to the output patch.
 */
@Slf4j
@Profile("demo")
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
    Function<SPMainMenuPanel, List<SettingsPanelDescriptor>> getOtherSettingsPanelFactory() {
        return parent -> Collections.singletonList(
                new SettingsPanelDescriptor(
                        new SPSettingPanel(parent, "Other Settings", SkyProcStarter.headerColor) {

                            LCheckBox importOnStartup;

                            @Override
                            protected void initialize() {
                                super.initialize();

                                importOnStartup = new LCheckBox("Import Mods on Startup", SkyProcStarter.settingsFont, SkyProcStarter.settingsColor);
                                importOnStartup.tie(YourSaveFile.Settings.IMPORT_AT_START, saveFile(), SUMGUI.helpPanel, true);
                                importOnStartup.setOffset(2);
                                importOnStartup.addShadow();
                                setPlacement(importOnStartup);
                                AddSetting(importOnStartup);

                                alignRight();
                            }
                        }, false, YourSaveFile.Settings.OTHER_SETTINGS)
                );
    }

    @Bean(name = "preProcessor")
    Runnable preProcessor() {
        return () -> log.info("pre-processor");
    }

    @Bean(name = "postProcessor")
    Runnable postProcessor() {
        return () -> log.info("post-processor");
    }

    @Bean(name = "customPatcher")
    Consumer<Mod> getCustomerPatcher() {
        return mod -> log.info("customer patcher consuming mod={}", mod);
    }

    @Bean(name = "save")
    SkyProcSave saveFile() {
        return new YourSaveFile();
    }
}
