package com.maple.maple.nexon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatResponse {
    private String name;
    private String combat_power;      // 42
    private String max_stat_ack;      // 1
    private String dmg;               // 2
    private String boss_dmg;          // 3
    private String fin_dmg;           // 4
    private String ign_dfn;           // 5
    private String cri_dng;           // 7
    private String Arcane_force;      // 14
    private String Authentic_force;   // 15
}
