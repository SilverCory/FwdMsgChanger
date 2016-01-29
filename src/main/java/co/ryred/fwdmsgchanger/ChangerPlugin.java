/*
 * http://ryred.co/
 * ace[at]ac3-servers.eu
 *
 * =================================================================
 *
 * Copyright (c) 2016, Cory Redmond
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of FwdMsgChanger nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package co.ryred.fwdmsgchanger;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * Created by Cory Redmond on 25/01/2016.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class ChangerPlugin extends JavaPlugin {

    public static String kick_message = "&c&lPlease join the proper ip!\n&5&lplay.myserver.com";

    @Override
    public void onLoad() {

        if (!new File(getDataFolder(), "kick_message.txt").exists())
            saveResource("kick_message.txt", false);
        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();
    }

    @Override
    public void onEnable() {

        try {
            kick_message = new Scanner(new FileInputStream(new File(getDataFolder(), "kick_message.txt")), "UTF-8").useDelimiter("\\A").next();
            kick_message = ChatColor.translateAlternateColorCodes('&', kick_message).replace("\n\r", "\n").replace("\r", "");
        } catch (Exception e) {
            throw new RuntimeException("Unable to load the kick_message.txt file.", e);
        }

        try {
            new PacketListener(this);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
