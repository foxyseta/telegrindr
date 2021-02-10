package bot;

import bot.data.Profile;
import bot.data.Stat;

import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
        return Reply.of(onSentLocationAction, Flag.LOCATION);
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

    private Profile getProfile(MessageContext ctx) {
        final Map<Integer, Profile> profiles =
            db.getMap(ctx.chatId().toString());
        final int id = ctx.user().getId();
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

    private Consumer<MessageContext> iamAction = ctx -> {
        final Profile profile = getProfile(ctx);
        for (String argument : ctx.arguments())     
            if (!update(profile, argument))
                silent.send(argument + " ❓", ctx.chatId());
    };

    private Consumer<Update> onSentLocationAction = upd -> {
        SendMessage message = new SendMessage();

        message.setChatId(Long.toString(upd.getMessage().getChatId()));
        
        message.setReplyToMessageId(upd.getMessage().getMessageId());

        message.setText("Should I set this as your current location?");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(new InlineKeyboardButton("✔️"));
        row.add(new InlineKeyboardButton("❌"));
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
        
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } 
    };

}