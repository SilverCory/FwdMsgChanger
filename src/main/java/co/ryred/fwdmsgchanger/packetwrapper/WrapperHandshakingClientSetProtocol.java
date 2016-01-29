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
package co.ryred.fwdmsgchanger.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Protocol;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperHandshakingClientSetProtocol extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Handshake.Client.SET_PROTOCOL;

    public WrapperHandshakingClientSetProtocol() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperHandshakingClientSetProtocol(PacketContainer packet) {
        super(packet, TYPE);
    }

    /**
     * Retrieve Protocol Version.
     * <p>
     * Notes: (4 as of 1.7.2)
     * @return The current Protocol Version
     */
    public int getProtocolVersion() {
        return handle.getIntegers().read(0);
    }

    /**
     * Set Protocol Version.
     * @param value - new value.
     */
    public void setProtocolVersion(int value) {
        handle.getIntegers().write(0, value);
    }

    /**
     * Retrieve Server Address (hostname or IP).
     * <p>
     * Notes: localhost
     * @return The current Server Address (hostname or IP)
     */
    public String getServerAddressHostnameOrIp() {
        return handle.getStrings().read(0);
    }

    /**
     * Set Server Address (hostname or IP).
     * @param value - new value.
     */
    public void setServerAddressHostnameOrIp(String value) {
        handle.getStrings().write(0, value);
    }

    /**
     * Retrieve Server Port.
     * <p>
     * Notes: 25565
     * @return The current Server Port
     */
    public int getServerPort() {
        return handle.getIntegers().read(1);
    }

    /**
     * Set Server Port.
     * @param value - new value.
     */
    public void setServerPort(int value) {
        handle.getIntegers().write(1, value);
    }

    /**
     * Retrieve Next state.
     * <p>
     * Notes: 1 for status, 2 for login
     * @return The current Next state
     */
    public Protocol getNextState() {
        return handle.getProtocols().read(0);
    }

    /**
     * Set Next state.
     * @param value - new value.
     */
    public void setNextState(Protocol value) {
        handle.getProtocols().write(0, value);
    }

}
