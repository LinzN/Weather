/*
 * Copyright (C) 2020. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.localWeather.callbacks;


import de.stem.stemSystem.STEMSystemApp;
import de.stem.stemSystem.taskManagment.operations.AbstractOperation;
import de.stem.stemSystem.taskManagment.operations.OperationOutput;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SensorRequestOperation extends AbstractOperation {

    @Override
    public OperationOutput runOperation() {
        OperationOutput operationOutput = new OperationOutput(this);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("request");
            operationOutput.setExit(0);
        } catch (IOException e) {
            e.printStackTrace();
            operationOutput.setExit(-1);
        }
        STEMSystemApp.getInstance().getZSocketModule().getzServer().getClients().values().forEach(serverConnection -> serverConnection.writeOutput("sensor_data", byteArrayOutputStream.toByteArray()));
        return operationOutput;
    }
}
