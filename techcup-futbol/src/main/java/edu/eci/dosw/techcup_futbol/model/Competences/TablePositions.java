package edu.eci.dosw.techcup_futbol.model.Competences;
import edu.eci.dosw.techcup_futbol.dtos.TableDTO;

import java.util.ArrayList;
import java.util.List;

import edu.eci.dosw.techcup_futbol.dtos.TableDTO;
import edu.eci.dosw.techcup_futbol.model.Games.Match;

public class TablePositions {

    /**
     * + calculateTable(games : List<Game>) : List<TableDTO>
     */
    public List<TableDTO> calculateTable(List<Match> games) {
        // Aquí iría la lógica para sumar puntos (3 victoria, 1 empate)
        // Por ahora devolvemos una lista vacía según la firma del diagrama
        List<TableDTO> standings = new ArrayList<>();
        return standings;
    }
}