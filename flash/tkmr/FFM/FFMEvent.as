package tkmr.FFM{
    import flash.events.*;

    public class FFMEvent extends Event{
        public static var MESSAGE_CALLBACK:String = "messageCallback";
        private var _data:*;

        public FFMEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false, data:*=null)
        {
            super(type, bubbles, cancelable);
            _data = data;
        }

        public function get data():*
        {
            return _data;
        }
    }
}