package e.gongfurui.digitallearnerlogbookV2.Listener;

@FunctionalInterface
public interface IPositiveNegativeDialogListener {

    void onPositiveClick();

    default void onNegativeClick() {

    }
}
