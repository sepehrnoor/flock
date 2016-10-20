package flock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * SEP'S ULTRA-REALISTIC BIRD FLOCK SIMULATOR
 * TOTALLY NOT A BOTCHED ATTEMPT AT MAKING BOIDS
 */
public class Main {

    public static void main(String[] args) {

        flock.Simulation sim;

        if (args != null && args.length > 0) {
            try {
                int quantity = Integer.parseInt(args[1]);
                sim = new Simulation(quantity);
            } catch (Exception e) {
                sim = new Simulation(100);
            }
        } else {
            sim = new flock.Simulation(100);
        }
        Thread simThread = new Thread(sim, "simThread");
        simThread.start();


        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            while (true) {
                String s = bufferedReader.readLine();
                if (s.toLowerCase().contains("help") || s.toLowerCase().contains("?")) {
                    String[] sp = s.split(" ");
                    if (sp.length < 2) {
                        System.out.println("Available commands: pause, resume, step, exit (quit), help (?), bounce, maxspeed");
                        System.out.println("Type help command name for info about command");
                    } else {
                        String command = sp[1];
                        if (command.toLowerCase().equals("pause")) {
                            System.out.println("Pauses the simulation");
                        } else if (command.toLowerCase().equals("resume")) {
                            System.out.println("Resumes the simulation");
                        } else if (command.toLowerCase().equals("exit") || command.toLowerCase().equals("quit")) {
                            System.out.println("Ends the simulation and exits the program");
                        } else if (command.toLowerCase().equals("help") || command.toLowerCase().equals("?")) {
                            System.out.println("Displays a list of commands, or displays info about a command");
                        } else if (command.toLowerCase().equals("bounce")) {
                            System.out.println("Enables/disables edge bounce.");
                        } else if (command.toLowerCase().equals("maxspeed")) {
                            System.out.println("Sets the max speed for individual boids.");
                        } else if (command.toLowerCase().equals("step")) {
                            System.out.println("Advances the simulation by a set number of frames. Takes one argument of type int. Only works when paused.");
                        } else {
                            System.out.println("Unknown command, type help for list of commands");
                        }
                    }
                } else if (s.toLowerCase().equals("pause")) {
                    sim.running = false;
                    System.out.println("Simulation paused");
                } else if (s.toLowerCase().equals("resume")) {
                    sim.running = true;
                    System.out.println("Simulation resumed");
                } else if (s.toLowerCase().contains("step")) {
                    if (!sim.running) {
                        String[] sp = s.split(" ");
                        try {
                            int steps = Integer.parseInt(sp[1]);
                            System.out.println("Attempting to step simulation by " + steps + " frames...");
                            for (int i = 0; i < steps; i++) {
                                sim.update();
                            }
                            sim.render();
                            System.out.println("Simulation was successfully advanced by " + steps + " steps");
                        } catch (Exception e) {
                            System.out.println("Please enter a valid argument (e.g. step 10)");
                        }
                    } else {
                        System.out.println("The simulation must be paused in order to step");
                    }
                } else if (s.toLowerCase().contains("bounce")) {
                    String[] sp = s.split(" ");
                    try {
                        if (sp[1].equals("on") || sp[1].equals("true")) {
                            sim.windowBounce = true;
                            System.out.println("Bounce enabled");
                        }
                        if (sp[1].equals("off") || sp[1].equals("false")) {
                            sim.windowBounce = false;
                            System.out.println("Bounce disabled");
                        }
                    } catch (Exception e) {
                        System.out.println("Please enter a valid argument (e.g. bounce on)");
                    }
                } else if (s.toLowerCase().contains("maxspeed")) {
                    String[] sp = s.split(" ");
                    try {
                        double maxspeed = Double.parseDouble(sp[1]);
                        sim.maxSpeed = maxspeed;
                        System.out.println("Max speed set to " + maxspeed);
                    } catch (Exception e) {
                        System.out.println("Please enter a valid argument (e.g. bounce on)");
                    }
                } else if (s.toLowerCase().equals("exit") || s.toLowerCase().equals("quit")) {
                    System.out.println("Shutting down gracefully...");
                    sim.shutdown();
                    System.exit(0);
                } else System.out.println("Please enter a valid command (type help for list of commands)");
            }
        } catch (IOException e) {

        }
    }
}
