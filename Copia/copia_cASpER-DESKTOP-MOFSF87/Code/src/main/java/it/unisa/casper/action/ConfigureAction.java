package it.unisa.casper.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import it.unisa.casper.gui.ConfigureThreshold;
import org.jetbrains.annotations.NotNull;

public class ConfigureAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        ConfigureThreshold config = new ConfigureThreshold();
        config.show();

    }

}