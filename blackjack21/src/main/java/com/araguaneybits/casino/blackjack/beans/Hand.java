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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jestevez
 */
public class Hand {

    private final List<Card> cards;
    private int score;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
        if ("A".equalsIgnoreCase(card.getRank())) {
            if (score <= 10) {
                score += 11;
            } else {
                score += 1;
            }
        } else {
            // No es "A" tomar el valor de la carta
            score += card.getValue();
        }
        // Recalcular para cambiar el valor del "A"
        if(score > 21) {
            score = 0;
            for (Card cardTmp : cards) {
                if ("A".equalsIgnoreCase(cardTmp.getRank())) {
                     score += 1;
                } else {
                    // No es "A" tomar el valor de la carta
                    score += cardTmp.getValue();
                }
            }
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isBlackjack() {
        return score == 21;
    }

    public boolean isGameOver() {
        return score > 21;
    }

}
