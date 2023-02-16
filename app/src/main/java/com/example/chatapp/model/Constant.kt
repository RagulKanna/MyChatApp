package com.example.chatapp.model

object Constant {
    const val LAST_MESSAGE = "lastMessage"
    const val CURRENT_USER_FCM_TOKEN = "currentUserFcmToken"
    const val VERIFICATION_ID = "VerificationId"
    const val CURRENT_PHONE_NUMBER = "currentUserPhoneNumber"
    const val CURRENT_USER_FIREBASE_UID = "currentUserFirebaseUid"
    const val CURRENT_USERNAME = "currentUserName"
    const val CURRENT_USER_PROFILE_PICTURE = "currentUserProfilePicture"
    const val GROUP_PROFILE_PICTURE = "groupProfilePicture"
    const val GROUP_NAME = "groupName"
    const val CHECK_USER_FLAG = "1"
    const val CURRENT_USER_STATUS = "currentUserStatus"
    const val CURRENT_RECEIVER_USER_NAME = "receiverUserName"
    const val CURRENT_RECEIVER_USER_ID = "receiverUserId"
    const val OTHER_USER_PROFILE_PICTURE = "receiverProfilePicture"
    private const val REMOTE_MSG_AUTHORIZATION = "Authorization"
    private const val REMOTE_MSG_CONTENT_TYPE = "Content-Type"
    const val REMOTE_MSG_DATA = "data"
    const val REMOTE_MSG_REGISTRATION_IDS = "registration_ids"
    const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY = "AAAAS_KzHmA:APA91bGnUwHry6MXixuKPxf1qM2cJ2yJlzHDlwuGj6_NO9E-OHm1xiUydexHArXzjE82ceMuUMsMsSdQQSnUVzBYlRraVfo2N_FRFlYlFUWpuy5OgPx4nWcN5ZEC1lUfTBKiaZ_oeRTn"
    const val CONTENT_TYPE = "application/json"

    var remoteMessageHeader: HashMap<String, String> = HashMap<String, String>()

    fun getRemoteHeader(): HashMap<String, String> {
        if (remoteMessageHeader.isEmpty()) {
            remoteMessageHeader.put(
                REMOTE_MSG_AUTHORIZATION,
                "key=AAAAS_KzHmA:APA91bGVc58FUC1pnDhG_kTkKnJJIhv_OYgLX9EtOj9yyphD8QJaZ2v83F6usdgaGkW0c0etiYoQShD7A_4fDBkPlDCQR4NZZAH00EkK0ZoQCozW-D1bZBtbkEQB77Q0a_Aa-lDy2R5B"
            )
            remoteMessageHeader[REMOTE_MSG_CONTENT_TYPE] = "application/json"
        }
        return remoteMessageHeader
    }
}