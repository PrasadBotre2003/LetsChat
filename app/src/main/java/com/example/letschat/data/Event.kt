package com.example.letschat.data

import java.security.KeyStore.TrustedCertificateEntry

open class Event<out  T>(val content:T) {
    var HasbeenHandeld = false
        fun getContentOrnull():T? {
  return if(HasbeenHandeld) null
            else{
                HasbeenHandeld = true
      content

  }
        }
}