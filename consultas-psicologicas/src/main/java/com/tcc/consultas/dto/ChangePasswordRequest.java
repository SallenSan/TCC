package com.tcc.consultas.dto;

public record ChangePasswordRequest(String oldPassword, String newPassword) {}
