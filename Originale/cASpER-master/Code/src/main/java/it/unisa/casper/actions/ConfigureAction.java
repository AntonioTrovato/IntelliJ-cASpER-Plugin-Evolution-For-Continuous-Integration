package it.unisa.casper.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import it.unisa.casper.gui.ConfigureThreshold;
import org.jetbrains.annotations.NotNull;

public class ConfigureAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        System.out.println("ConfigureAction");
        ConfigureThreshold config = new ConfigureThreshold();
        config.show();

    }

}