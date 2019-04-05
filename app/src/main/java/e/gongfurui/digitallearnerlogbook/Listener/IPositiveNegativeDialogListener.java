package e.gongfurui.digitallearnerlogbook.Listener;

@FunctionalInterface
public interface IPositiveNegativeDialogListener {

    void onPositiveClick();

    default void onNegativeClick() {

    }
}
