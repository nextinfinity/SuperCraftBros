/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.nextinfinity.supercraftbros.util;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Colorizer {

    public static ItemStack setColor(ItemStack item, int r, int g, int b){
        LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
        lam.setColor(Color.fromRGB(r, g, b));
        item.setItemMeta(lam);
        return item;
    }
}
