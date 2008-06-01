package tkmr.FFM{
    public class ISocket{
        public function ISocket(){ }

        public function connect(url:String, room:String){
        }

        public function send(room:String, message:Object):Bool{
            return true;
        }

        public function addMessageCallback(room:String, callback:Function):void {
            addEventListener(FFMEvent.MESSAGE_CALLBACK + "_" + room, callback);
        }
    }
}