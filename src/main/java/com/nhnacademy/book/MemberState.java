package com.nhnacademy.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_state_id")
    private long memberStateId;

    @Column(name = "name", nullable = false, columnDefinition = "DEFAULT 'ACTIVE'")
    private StatusName memberStateName;
}
