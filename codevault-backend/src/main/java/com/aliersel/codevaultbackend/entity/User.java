package com.aliersel.codevaultbackend.entity;

import com.aliersel.codevaultbackend.constant.enums.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    Integer userID;
    String userName;
    String passwordHash;
    AppUserRole role;
    OffsetDateTime dateRegistered;
    String phoneNumber;
    String email;
    Integer acEasy;
    Integer acMedium;
    Integer acHard;
    Integer totalEasy;
    Integer totalMedium;
    Integer totalHard;
}
