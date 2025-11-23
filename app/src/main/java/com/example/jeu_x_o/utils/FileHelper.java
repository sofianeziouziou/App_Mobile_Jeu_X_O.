package com.example.jeu_x_o.utils;

import android.content.Context;
import com.example.jeu_x_o.model.TournamentResult;
import java.io.*;

public class FileHelper {
    private static final String FILENAME = "last_tournament.dat";

    public static boolean saveTournament(Context ctx, TournamentResult tr) {
        try (FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tr);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static TournamentResult loadTournament(Context ctx) {
        try (FileInputStream fis = ctx.openFileInput(FILENAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (TournamentResult) ois.readObject();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteTournamentFile(Context ctx) {
        return ctx.deleteFile(FILENAME);
    }
}