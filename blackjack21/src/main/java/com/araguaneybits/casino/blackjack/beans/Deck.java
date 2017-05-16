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
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jestevez
 */
public class Deck {

    private final List<Card> cards;

    public Deck() {
        // Crear paquete de 52 cartas
        this.cards = new ArrayList<>();
        for (Suits p : Suits.values()) {
            //System.out.printf("Suits %s\n", p);
            int i = 0;
            for (FaceCards f : FaceCards.values()) {
                Card card = new Card();
                card.setSuit(p.toString());
                card.setRank(f.getRank());
                card.setSort(i);
                card.setValue(f.getValue());
                //System.out.printf("Face %s - %s\n", f.getRank(), f.getValue());
                i++;
                cards.add(card);
            }
        }
        
        // Barajear las cartas
        Random random = new Random();
        Collections.shuffle(cards, random);

    }

    public List<Card> getCards() {
        return cards;
    }
    
    public Card dealCard() {
        return cards.remove(0);
    }

}
