package bot;

import bot.data.Profile;
import bot.data.Stat;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

enum CallbackQuery {

    CONFIRMLOCATION("confirmLocation"),
    CANCELLOCATION("cancelLocation");
    
    CallbackQuery(String data) {
        this.data = data;
    }
    
    public String data() {
        return data;
    }

    private String data;

    public Predicate<Update> isQueried = upd -> {
        return upd.hasCallbackQuery() &&
               upd.getCallbackQuery().getData().equals(data);
    };

}

public class Macinino extends AbilityBot {

    public Macinino(String botToken, String username, int creatorId) {
        super(botToken, username);
        cId = creatorId;
    }

    @Override
    public int creatorId() {
        return cId;
    }

    public Ability iam() {
        return Ability
                .builder()
                .name("iam")
                .info("edits your profile")
                .input(0)
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .action(iamAction)
                .enableStats()
                .build();
    }

    public Reply onSentLocation() {
        return Reply.of(onSentLocationAction.andThen(onSentLocationReply),
                        Flag.LOCATION);
    }

    public Reply onConfirmedLocation() {
        return Reply.of(onConfirmedLocationAction,
                        CallbackQuery.CONFIRMLOCATION.isQueried);
    }

    private int cId;
    final private static String
        TAGARGUMENTREGEX = "([+-]?)#(0-9A-Za-z+).*",
        STATARGUMENTREGEX = "(\\d+)(.+)",
        RANGEARGUMENTREGEX = "(\\d*),?(\\d*)(.+)";
    final private static Pattern
        TAGARGUMENTPATTERN = Pattern.compile(TAGARGUMENTREGEX),
        STATARGUMENTPATTERN = Pattern.compile(STATARGUMENTREGEX),
        RANGEARGUMENTPATTERN = Pattern.compile(RANGEARGUMENTREGEX);

    private Profile getProfile(Long chatId, int userId) {
        final Map<Integer, Profile> profiles =
            db.getMap(chatId.toString());
        final int id = userId;
        if (!profiles.containsKey(id))
            return profiles.put(id, new Profile(id));
        else
            return profiles.get(id);
    }

    private boolean update(Profile profile, String argument) {
        Matcher matcher = TAGARGUMENTPATTERN.matcher(argument);
        if (matcher.matches()) {
            String tag = matcher.group(2);
            if (matcher.group(1).equals("-"))
                profile.removeTag(tag);
            else
                profile.addTag(tag);
        } else {
            matcher = STATARGUMENTPATTERN.matcher(argument);
            if (matcher.matches()) {
                final String uom = matcher.group(2);
                for (Stat stat : Stat.values())
                    if (stat.uom().equals(uom)) {
                        final int value = Integer.parseInt(matcher.group(1));
                        if (stat.validate(value))
                            profile.putStat(stat, value);
                        break;
                    }
            } else {
                matcher = Profile.EMOJIPATTERN.matcher(argument);
                if (matcher.matches())
                    profile.setEmoji(argument);
                else
                    return false;
            }
        }
        return true;
    }

    final private Consumer<MessageContext> iamAction = ctx -> {
        final Profile profile = getProfile(ctx.chatId(), ctx.user().getId());
        for (String argument : ctx.arguments())     
            if (!update(profile, argument))
                silent.send(argument + " ❓", ctx.chatId());
    };

    final private Consumer<Update> onSentLocationAction = upd -> {
        getProfile(upd.getMessage().getChatId(),
                   upd.getMessage().getFrom().getId()).lastSentLocation =
            upd.getMessage().getLocation();
    };

    final private Consumer<Update> onSentLocationReply = upd -> {
        SendMessage message = new SendMessage();

        message.setChatId(Long.toString(upd.getMessage().getChatId()));
        
        message.setReplyToMessageId(upd.getMessage().getMessageId());

        message.setText("Should I set this as your current location?");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton confirmLocationButton = new InlineKeyboardButton("✔️");
        confirmLocationButton.setCallbackData(
            CallbackQuery.CONFIRMLOCATION.data()
        );
        row.add(confirmLocationButton);
        InlineKeyboardButton cancelLocationButton = new InlineKeyboardButton("❌");
        cancelLocationButton.setCallbackData(
            CallbackQuery.CANCELLOCATION.data()
        );
        row.add(cancelLocationButton);
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } 
    };
    
    final private Consumer<Update> onConfirmedLocationAction = upd -> {
        final Profile profile = getProfile(upd.getMessage().getChatId(),
                   upd.getMessage().getFrom().getId());
        profile.location = profile.lastSentLocation;
        profile.lastSentLocation = null;
    }; 

}