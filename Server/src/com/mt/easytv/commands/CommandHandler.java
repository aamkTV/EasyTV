package com.mt.easytv.commands;

import java.io.BufferedReader;
import java.util.ArrayList;

public final class CommandHandler
{
    private ArrayList<Command>        _commands = new ArrayList<>();
    private ArrayList<BufferedReader> _readers  = new ArrayList<>();

    public void addCommand(Command command)
    {
        this._commands.add(command);
    }

    public void addCommand(String commandName, ICommand action) {
        Command command = new Command() {
            @Override
            public void processCommand(CommandArgument[] args) {
                action.processCommand(args);
            }
        };
        command.Command = commandName;

        this.addCommand(command);
    }

    public boolean removeCommand(String commandName) {
        for(Command command : this._commands) {
            if(commandName.equals(command.Command)) {
                this.removeCommand(command);
                return true;
            }
        }

        return false;
    }

    public boolean removeCommand(Command command) {
        if(!this._commands.contains(command)) {
            return false;
        }

        this._commands.remove(command);
        return true;
    }

    public void processCommand(String commandFull) throws CommandNotFoundException {
        /* Building up argument array */
        String[] commandParts = commandFull.split(" ");
        String[] argumentParts = new String[commandParts.length - 1];
        System.arraycopy(commandParts, 1, argumentParts, 0, commandParts.length - 1);
        CommandArgument[] args = CommandArgument.fromArray(argumentParts);

        for(Command command : this._commands) {
            if(command.Command.equals(commandParts[0])) {
                command.processCommand(args);
                break;
            }
        }
    }

    public void addReader(BufferedReader reader) {
        this._readers.add(reader);
    }

    public boolean removeReader(BufferedReader reader) {
        if(!this._readers.contains(reader)) {
            return false;
        }

        this._readers.remove(reader);
        return true;
    }

    public void read() throws Exception {
        for(BufferedReader reader : this._readers) {
            this.readFrom(reader);
        }
    }

    public void readFrom(BufferedReader reader) throws Exception {
        this.processCommand(reader.readLine());
    }
}