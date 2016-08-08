# HttpUploadComponent
Xmpp ExternalComponent For File Upload


The HttpUploadComponent is a plugin extension to your XMPP server that allows users to upload files to a HTTP host and eventually share the link to those files.

It runs as a stand alone process on the same host as your XMPP server and connects to that server using the [abber Component Protocol](http://xmpp.org/extensions/xep-0114.html).

A detailed introduction into the necessity of such a component and the simple protocol can be found in [XEP-0363: HTTP File Upload](http://xmpp.org/extensions/xep-0363.html).

#File Server
From [https://github.com/jeozey/fileserver](https://github.com/jeozey/fileserver)
Fork From [https://github.com/polopolyps/fileserver](https://github.com/polopolyps/fileserver) and Modify not to change the URL and FileName for Xep-0363,and it should support https.
