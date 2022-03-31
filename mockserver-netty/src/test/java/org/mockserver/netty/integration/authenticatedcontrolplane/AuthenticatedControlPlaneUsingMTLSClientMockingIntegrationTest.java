package org.mockserver.netty.integration.authenticatedcontrolplane;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.testing.integration.mock.AbstractBasicMockingSameJVMIntegrationTest;

import static org.mockserver.configuration.ConfigurationProperties.*;
import static org.mockserver.stop.Stop.stopQuietly;

/**
 * @author jamesdbloom
 */
public class AuthenticatedControlPlaneUsingMTLSClientMockingIntegrationTest extends AbstractBasicMockingSameJVMIntegrationTest {

    private static String originalControlPlaneTLSMutualAuthenticationCAChain;
    private static String originalControlPlanePrivateKeyPath;
    private static String originalControlPlaneX509CertificatePath;
    private static boolean originalControlPlaneTLSMutualAuthenticationRequired;

    @BeforeClass
    public static void startServer() {
        // save original value
        originalControlPlaneTLSMutualAuthenticationCAChain = controlPlaneTLSMutualAuthenticationCAChain();
        originalControlPlanePrivateKeyPath = controlPlanePrivateKeyPath();
        originalControlPlaneX509CertificatePath = controlPlaneX509CertificatePath();
        originalControlPlaneTLSMutualAuthenticationRequired = controlPlaneTLSMutualAuthenticationRequired();

        // set new certificate authority values
        controlPlaneTLSMutualAuthenticationCAChain("org/mockserver/netty/integration/tls/ca.pem");
        controlPlanePrivateKeyPath("org/mockserver/netty/integration/tls/leaf-key-pkcs8.pem");
        controlPlaneX509CertificatePath("org/mockserver/netty/integration/tls/leaf-cert.pem");
        controlPlaneTLSMutualAuthenticationRequired(true);

        mockServerClient = ClientAndServer.startClientAndServer().withSecure(true);
    }

    @AfterClass
    public static void stopServer() {
        stopQuietly(mockServerClient);

        // set back to original value
        controlPlaneTLSMutualAuthenticationCAChain(originalControlPlaneTLSMutualAuthenticationCAChain);
        controlPlanePrivateKeyPath(originalControlPlanePrivateKeyPath);
        controlPlaneX509CertificatePath(originalControlPlaneX509CertificatePath);
        controlPlaneTLSMutualAuthenticationRequired(originalControlPlaneTLSMutualAuthenticationRequired);
    }

    @Override
    public int getServerPort() {
        return mockServerClient.getPort();
    }

    @Override
    protected boolean isSecureControlPlane() {
        return true;
    }

}
