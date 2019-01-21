/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package net.nextinfinity.supercraftbros;

import org.bukkit.Location;

public class Arena {

    Location min;
    Location max;

    public Arena(Location min, Location max) {
        this.max = max;
        this.min = min;
    }

    public boolean containsBlock(Location v) {
        if (v.getWorld() != min.getWorld()) return false;
        final double x = v.getX();
        final double y = v.getY();
        final double z = v.getZ();
        return x >= min.getBlockX() && x < max.getBlockX() + 1 && y >= min.getBlockY() && y < max.getBlockY() + 1 && z >= min.getBlockZ() && z < max.getBlockZ() + 1;
                
    }

    public Location getMax() {
        return max;
    }

    public Location getMin() {
        return min;
    }
}