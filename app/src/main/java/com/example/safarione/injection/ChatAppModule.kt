package com.example.safarione.injection

import android.content.Context
import com.example.safarione.module.credential.CredentialManager
import com.example.safarione.module.credential.impl.CredentialManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatAppModule {

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManagerImpl(context)

    @Provides
    @Singleton
    @Named(InjectionNamed.XMPP_DOMAIN)
    fun provideXmppDomain() : String = "uatchat2.waafi.com"

    @Provides
    @Singleton
    fun provideChatServiceConfiguration(@Named(InjectionNamed.XMPP_DOMAIN) domain: String) : XMPPTCPConnectionConfiguration =
        XMPPTCPConnectionConfiguration.builder()
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            .setPort(5222)
            .setXmppDomain(domain)
            .build()
}