package com.hstech.messenger.models

class MessageObject(val isOnline: Boolean)
{
    var sender: String? = null
    var time: Long? = null
    var messageText: String? = null
    var messageContent: ArrayList<MessageContent>? = ArrayList()
}

class MessageContent(val messageBody: ByteArray?, val messageType: String?)