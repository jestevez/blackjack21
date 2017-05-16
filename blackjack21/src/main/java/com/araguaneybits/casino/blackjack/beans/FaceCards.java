/*
 * Copyright 2017 AraguaneyBits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.araguaneybits.casino.blackjack.beans;

/**
 *
 * @author jestevez
 */
public enum FaceCards {
    TWO("2", 2),
    THREE("3", 3),
    FOUR("4", 4),
    FIVE("5", 5),
    SIX("6", 6),
    SEVEN("7", 7),
    EIGHT("8", 8),
    NINE("9", 9),
    TEN("10", 10),
    J("J", 10),
    Q("Q", 10),
    K("K", 10),
    A("A", 11);

    private final String rank;
    private final int value;

    private FaceCards(String rank, int value) {
        this.rank = rank;
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

}
