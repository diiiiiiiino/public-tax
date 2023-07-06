package com.nos.tax;

import jakarta.persistence.EntityManager;

public class TestUtils {
    public static void flushAndClear(EntityManager entityManager) {
        entityManager.flush();
        entityManager.clear();
    }
}
