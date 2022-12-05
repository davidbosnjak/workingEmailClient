import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import javax.speech.Engine;
import javax.speech.Central;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Synthesizer;
import java.util.Locale;


public class TextToSpeech{

    public static void main(String[] args)
    {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");

        Voice[] voiceList = VoiceManager.getInstance().getVoices();
        for(int i =0; i<voiceList.length; i++){
            System.out.println("Voice: "+voiceList[i].getName());
        }
        if(voice !=null){
            voice.allocate();
            System.out.println("Voice rate: "+voice.getRate());
            System.out.println("Voice pitch: "+voice.getPitch());
            System.out.println("Voice volume: "+voice.getVolume());
            boolean status = voice.speak("Hello this is a test try will this actually work or nah");
            System.out.println("Status "+status);
            voice.deallocate();


        }
        else{
            System.out.println("smt didnt work");
        }


    }
    public static void speakPhrase(String phrase){
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        voice.allocate();
        voice.speak(phrase);
        voice.deallocate();
    }


}