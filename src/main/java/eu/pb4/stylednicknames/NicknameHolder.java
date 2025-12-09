package eu.pb4.stylednicknames;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public interface NicknameHolder {
    NicknameHolder EMPTY = new NicknameHolder() {
        @Override
        public void styledNicknames$set(String nickname, boolean requirePermission) {
        }

        @Override
        public @Nullable String styledNicknames$get() {
            return null;
        }

        @Override
        public @Nullable Component styledNicknames$getParsed() {
            return null;
        }

        @Override
        public @Nullable MutableComponent styledNicknames$getOutput() {
            return null;
        }

        @Override
        public MutableComponent styledNicknames$getOutputOrVanilla() {
            return Component.empty();
        }

        @Override
        public boolean styledNicknames$requiresPermission() {
            return false;
        }

        @Override
        public void styledNicknames$loadData() {}

        @Override
        public boolean styledNicknames$shouldDisplay() {
            return false;
        }

        @Override
        public Function<String, Component> styledNicknames$placeholdersCommand() {
            return x -> Component.empty();
        }
    };

    static NicknameHolder of(ServerPlayer player) {
        return (NicknameHolder) player.connection;
    }

    static NicknameHolder of(ServerGamePacketListenerImpl handler) {
        return (NicknameHolder) handler;
    }

    static NicknameHolder of(Object possiblePlayer) {
        if (possiblePlayer instanceof ServerPlayer player) {
            return (NicknameHolder) player.connection;
        }
        return EMPTY;
    }

    void styledNicknames$set(String nickname, boolean requirePermission);

    @Nullable
    String styledNicknames$get();

    @Nullable
    Component styledNicknames$getParsed();

    @Nullable
    MutableComponent styledNicknames$getOutput();

    MutableComponent styledNicknames$getOutputOrVanilla();

    boolean styledNicknames$requiresPermission();

    void styledNicknames$loadData();

    boolean styledNicknames$shouldDisplay();

    Function<String, Component> styledNicknames$placeholdersCommand();
}
