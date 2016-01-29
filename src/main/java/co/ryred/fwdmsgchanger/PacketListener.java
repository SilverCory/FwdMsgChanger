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

import co.ryred.fwdmsgchanger.packetwrapper.WrapperHandshakingClientSetProtocol;
import co.ryred.fwdmsgchanger.packetwrapper.WrapperLoginServerDisconnect;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.server.SocketInjector;
import com.comphenix.protocol.injector.server.TemporaryPlayerFactory;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Cory Redmond on 25/01/2016.
 *
 * @author Cory Redmond <ace@ac3-servers.eu>
 */
public class PacketListener extends PacketAdapter {

    private final ChangerPlugin pl;
    private final WrapperLoginServerDisconnect disconWrapper;
    private final Field bungeeField;

    public PacketListener(ChangerPlugin plugin) throws ClassNotFoundException, NoSuchFieldException {

        super(plugin, ListenerPriority.LOWEST, new ArrayList<>(Arrays.asList(PacketType.Handshake.Client.SET_PROTOCOL)), ListenerOptions.INTERCEPT_INPUT_BUFFER);
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
        this.pl = plugin;

        this.bungeeField = Class.forName("org.spigotmc.SpigotConfig").getDeclaredField("bungee");

        disconWrapper = new WrapperLoginServerDisconnect();
        disconWrapper.setReason(WrappedChatComponent.fromText(ChangerPlugin.kick_message));

    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        try {

            WrapperHandshakingClientSetProtocol protSet = new WrapperHandshakingClientSetProtocol(event.getPacket());

            String[] string = protSet.getServerAddressHostnameOrIp().split("\00");
            if (protSet.getNextState() == PacketType.Protocol.LOGIN && (boolean) bungeeField.get(null) && string.length != 3 && string.length != 4) {
                SocketInjector ignored = TemporaryPlayerFactory.getInjectorFromPlayer(event.getPlayer());
                ignored.sendServerPacket(disconWrapper.getHandle().getHandle(), event.getNetworkMarker(), false);
                pl.getLogger().info("Disconnected " + ignored.getSocket().getInetAddress() + ":" + ignored.getSocket().getPort() + ". They weren't using IP Forwarding.");
                event.setCancelled(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
