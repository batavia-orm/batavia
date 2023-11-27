package com.batavia.orm.cli;
import com.batavia.orm.runner.RunnerMain;

public class RevertCommand implements Command{
    @Override
    public void execute() {
        System.out.println("Migrating...");

        //with the migration file as an argument
        RunnerMain.main(new String[]{});
    }
}