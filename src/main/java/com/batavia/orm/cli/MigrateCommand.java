package com.batavia.orm.cli;
import com.batavia.orm.runner.RunnerMain;

// Concrete command for migrating
public class MigrateCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Migrating...");

        //with the migration file as an argument
        RunnerMain.main(new String[]{});
    }
}