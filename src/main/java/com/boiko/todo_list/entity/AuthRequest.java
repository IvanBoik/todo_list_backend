package com.boiko.todo_list.entity;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String user;
    private String password;
}
