package android.wigs.android.mediasample;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    /**
     * メディアプレイヤーフィールド
     */
    private MediaPlayer _player;

    /**
     *　再生・一時停止ボタンフィールド
     */
    private Button _btPlay;

    /**
     *　戻るボタンフィールド
     */
    private Button _btBack;
    /**
     *　進ボタンフィールド
     */
    private Button _btForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //フィールドの各ボタンを取得。
        _btPlay = findViewById(R.id.btPlay);
        _btBack = findViewById(R.id.btBack);
        _btForward = findViewById(R.id.btForward);

        //フィールドのメディアプレーヤーオブジェクトを生成。
        _player = new MediaPlayer();
        //音声ファイルのURI文字列を作成。
        String mediaFileUriStr = "android.resource://"+ getPackageName() + "/"+
                R.raw.mountain_stream;
        //音声ファイルのURI文字列をもとにURIオブジェクトを生成。
        Uri mediaFileUri = Uri.parse(mediaFileUriStr);
        try {
            //メディアプレーヤーに音声ファイルを指定。
            _player.setDataSource(MainActivity.this,mediaFileUri);
            //非同期でのメディア再生準備が完了したときのリスナを設定。
            _player.setOnPreparedListener(new PlayerPreparedListener());
            //メディア再生が終了した際のリスナを設定。
            _player.setOnCompletionListener(new PlayerCompletionListener());
            //非同期でのメディア再生を準備。
            _player.prepareAsync();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //スイッチを取得。
        Switch loopSwitch = findViewById(R.id.swLoop);
        //スイッチにリスナを登録。
        loopSwitch.setOnCheckedChangeListener(new LoopSwitchChangedListener());
    }
    /**
     * プレーヤーの再生準備が整ったときのリスナクラス。
     */
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp){
            //各ボタンをタップ可能に設定。
            _btPlay.setEnabled(true);
            _btBack.setEnabled(true);
            _btForward.setEnabled(true);
        }
    }
    /**
     * 再生が終了したときのリスナクラス
     */
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp){
            //ループ設定がされていないならば・・・
            if (!_player.isLooping()){
                //再生ボタンのラベルを「再生」に設定。
                _btPlay.setText(R.string.bt_play_play);
            }
        }
    }

    public void onPlayButtonClick(View view){
        //プレーヤーが再生中だったら・・・
        if (_player.isPlaying()){
            //プレーヤーを一時停止。
            _player.pause();
            //再生ボタンのラベルを「再生」に設定。
            _btPlay.setText(R.string.bt_play_play);
        }
        //プレーヤーが再生中じゃなかったら・・・
        else {
            _player.start();
            //再生ボタンのラベルを「一時停止」に設定。
            _btPlay.setText(R.string.bt_play_pause);
        }
    }

    @Override
    protected void onDestroy(){
        //親クラスのメソッドを呼び出し。
        super.onDestroy();
        //プレーヤーが再生中なら・・・
        if (_player.isPlaying()){
            //プレーヤーを停止。
            _player.stop();
        }
        //プレーヤーを解放。
        _player.release();
        //プレイヤー用フィールドをnullに。
        _player = null;
    }
    public void onBackButtonClick(View view){
        //再生位置を先頭に変更。
        _player.seekTo(0);
    }
    public void onForwardButtonClick(View view){
        //現在再生中のメディアファイルの長さを取得。
        int duration = _player.getDuration();
        //再生位置を終端に変更。
        _player.seekTo(duration);
        //再生中でないなら・・・
        if (!_player.isPlaying()){
            //再生を開始。
            _player.start();
        }
    }

    private class LoopSwitchChangedListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
            //ループするかどうかを設定。
            _player.setLooping(isChecked);
        }
    }
}