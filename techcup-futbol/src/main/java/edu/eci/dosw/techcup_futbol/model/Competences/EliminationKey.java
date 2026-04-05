package edu.eci.dosw.techcup_futbol.model.Competences;

import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.techcup_futbol.model.Games.Match;
import edu.eci.dosw.techcup_futbol.model.Teams.Team;

public class EliminationKey {
    private String round; // Ej: "Semicircles", "Final"

    public EliminationKey(String round) {
        this.round = round;
    }

    /**
     * + generateKeys(teams : List<Team>) : List<Game>
     * Crea los emparejamientos de forma aleatoria o por mérito.
     */
    public List<Match> generateKeys(List<Team> teams) {
        List<Match> matches = new ArrayList<>();
        // Lógica para emparejar equipos 1 vs 2, 3 vs 4...
        return matches;
    }
}