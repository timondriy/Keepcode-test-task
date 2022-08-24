package ru.govorukhin.thirdTask;

import java.net.InetSocketAddress;

public class Simplify {
    void processTask(ChannelHandlerContext ctx) {
        InetSocketAddress lineAddress = new InetSocketAddress(getIpAddress(), getUdpPort());
        int port = lineAddress.getPort();
        String hostString = lineAddress.getHostString();
        String hostName = lineAddress.getHostName();
        CommandType typeToRemove;

        for (Command currentCommand : getAllCommands()) {
            var commandType = currentCommand.getCommandType();
            var commandText = currentCommand.getCommandText();
            if ((commandType == CommandType.REBOOT_CHANNEL && !currentCommand.isAttemptsNumberExhausted() && currentCommand.isTimeToSend()) ||
                    commandType != CommandType.REBOOT_CHANNEL && !currentCommand.isAttemptsNumberExhausted()) {
                sendCommandToContext(ctx, lineAddress, commandText);
                try {
                    AdminController.getInstance().processUssdMessage(
                            new DblIncomeUssdMessage(hostName, port, 0, EnumGoip.getByModel(getGoipModel()), commandText), false);
                } catch (Exception ignored) {
                }
                currentCommand.setSendDate(new Date());
                Log.ussd.write(String.format("sent: ip: %s; порт: %d; %s", hostString, port, commandText));
                currentCommand.incSendCounter();
            } else {
                deleteCommand(commandType);
            }
            sendKeepAliveOkAndFlush(ctx);
        }
    }
}