package tkmr.FFM{
    import flash.events.*;

    public class Messanger extends EventDispatcher{
        private var room:String;
        private var socketType:String;

        public function Messanger(room:String, socket:ISocket)
        {

        }

        public function listen(room:String, callback:Function):Bool
        {
            return this.socket.connect(room, callback);
        }

        public function send(room:String, message:Object):Bool
        {
            return this.socket.send(room, message);
        }
    }
}