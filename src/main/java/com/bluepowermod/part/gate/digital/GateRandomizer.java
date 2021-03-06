/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate.digital;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTaintedSiliconChip;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateRandomizer extends GateSimpleDigital {

    private static final Random random = new Random();

    private int ticks = 0;

    private final boolean out[] = new boolean[3];

    private GateComponentTaintedSiliconChip c1, c2, c3;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable().setOutputOnly();
        right().enable().setOutputOnly();
        back().enable();
    }

    @Override
    public void initComponents() {

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(c2 = new GateComponentTaintedSiliconChip(this, 0xd6ab17));
        addComponent(c1 = new GateComponentTaintedSiliconChip(this, 0x0000FF));
        addComponent(c3 = new GateComponentTaintedSiliconChip(this, 0x00ccff));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "randomizer";
    }

    @Override
    public void tick() {

        if (!getWorld().isRemote) {
            if (back().getInput()) {
                if (ticks % 5 == 0) {
                    out[0] = random.nextBoolean();
                    out[1] = random.nextBoolean();
                    out[2] = random.nextBoolean();
                    left().setOutput(out[0]);
                    front().setOutput(out[1]);
                    right().setOutput(out[2]);
                    c1.setState(out[0]);
                    c2.setState(out[1]);
                    c3.setState(out[2]);
                }
                ticks++;
            } else {
                ticks = 0;
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("out_0", out[0]);
        tag.setBoolean("out_1", out[1]);
        tag.setBoolean("out_2", out[2]);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        out[0] = tag.getBoolean("out_0");
        out[1] = tag.getBoolean("out_1");
        out[2] = tag.getBoolean("out_2");
    }

    @Override
    public void doLogic() {

    }
}
