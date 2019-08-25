package com.hs.messenger.models

class Message
{
    var sender: String? = null
    var time: Long? = null
    var messageText: String? = null
    var messageContent: ArrayList<MessageContent>? = ArrayList()
}

class MessageContent(val messageBody: ByteArray?, val messageType: String?)