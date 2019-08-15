package com.hstech.messenger.models

class Message(sender: String,
              time: Long,
              seenStatus: String,
              messages: ArrayList<MessageContent>)

class MessageContent(messageBody: ByteArray, messageType: String)