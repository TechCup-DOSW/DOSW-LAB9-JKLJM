package edu.eci.dosw.techcup_futbol.dtos;

import java.util.List;

/**
 * DTO used to create or update a team.
 */
public class TeamDTO {

    private String name;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;
    private Integer captainId;
    private List<Integer> playerIds;

    public TeamDTO() {
    }

    public TeamDTO(String name, String logoUrl, String primaryColor,
                   String secondaryColor, Integer captainId, List<Integer> playerIds) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.captainId = captainId;
        this.playerIds = playerIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public Integer getCaptainId() {
        return captainId;
    }

    public void setCaptainId(Integer captainId) {
        this.captainId = captainId;
    }

    public List<Integer> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<Integer> playerIds) {
        this.playerIds = playerIds;
    }
}
