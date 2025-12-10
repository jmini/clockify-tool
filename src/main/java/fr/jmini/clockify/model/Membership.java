package fr.jmini.clockify.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "userId",
        "hourlyRate",
        "costRate",
        "targetId",
        "membershipType",
        "membershipStatus",
})
public class Membership {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("hourlyRate")
    private String hourlyRate;

    @JsonProperty("costRate")
    private String costRate;

    @JsonProperty("targetId")
    private String targetId;

    @JsonProperty("membershipType")
    private String membershipType;

    @JsonProperty("membershipStatus")
    private String membershipStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getCostRate() {
        return costRate;
    }

    public void setCostRate(String costRate) {
        this.costRate = costRate;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(String membershipStatus) {
        this.membershipStatus = membershipStatus;
    }
}
